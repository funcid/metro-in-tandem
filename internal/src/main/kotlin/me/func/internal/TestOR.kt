import com.google.ortools.Loader
import com.google.ortools.sat.BoolVar
import com.google.ortools.sat.Constraint
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.CpSolver
import com.google.ortools.sat.Literal
import com.google.ortools.sat.IntVar
import com.google.ortools.sat.CpSolverStatus
import com.google.ortools.sat.IntervalVar
import com.google.ortools.sat.LinearArgument
import com.google.ortools.sat.LinearExpr
import java.util.UUID
import kotlin.math.asin

data class Task(
    val id: Int,
    val requiredProperties: List<String>,
    val timeStart: Int,
    val timeEnd: Int,
    val locationStart: String,
    val locationEnd: String
)

data class Worker(
    val id: Int,
    val properties: List<String>,
    val startsWorking: Int,
    val endsWorking: Int
)

fun travelTime(loc1: String, loc2: String): Int {
    return 1  // Assume it takes 1 time unit to travel between any two locations
}

const val LUNCH_PAD_LEFT = 3
const val LUNCH_PAD_RIGHT = 2
fun main() {
    // Load native libraries for OR-Tools (make sure to set the path to the library properly)
    Loader.loadNativeLibraries()

    val tasks = listOf(
        Task(1, listOf("skill1"), 9, 11, "L1", "L2"),
        Task(2, listOf("skill2"), 10, 12, "L2", "L3"),
        Task(3, listOf("skill1", "skill2"), 13, 15, "L3", "L4"),
        Task(4, listOf("skill1", "skill2"), 13, 15, "L3", "L4"),
        Task(5, listOf("skill1", "skill2"), 13, 15, "L3", "L4"),
        // Task(6, listOf("skill1", "skill2"), 13, 15, "L3", "L4"),
    )

    val workers = listOf(
        Worker(1, listOf("skill1"), 9, 20),
        Worker(2, listOf("skill2"), 9, 20),
        Worker(3, listOf("skill1", "skill2"), 9, 20),
    )

    val model = CpModel()

    val assignment = HashMap<Pair<Int, Int>, Literal>()
    for (worker in workers) {
        for (task in tasks) {
            assignment[Pair(worker.id, task.id)] = model.newBoolVar("assign_w${worker.id}_t${task.id}")
        }
    }

    for (task in tasks) {
        // val requiredProperties = task.requiredProperties.toSet()
        val taskWorkers = workers/*.filter { worker ->
            // requiredProperties.all { it in worker.properties }
        }*/.map { worker -> assignment[Pair(worker.id, task.id)]!! }
        // model.addEquality(LinearExpr.sum(taskWorkers.toTypedArray()), 1)
        model.addAtMostOne(taskWorkers.toTypedArray())
        model.maximize(LinearExpr.sum(taskWorkers.toTypedArray()))
    }

    for (worker in workers) {
        for (i in tasks.indices) {
            for (j in (i + 1) until tasks.size) {
                val task1 = tasks[i]
                val task2 = tasks[j]
                if (task1.timeEnd + travelTime(task1.locationEnd, task2.locationStart) > task2.timeStart) {
                    model.addBoolOr(
                        listOf(
                            assignment[Pair(worker.id, task1.id)]!!.not(),
                            assignment[Pair(worker.id, task2.id)]!!.not()
                        )
                    )
                }
            }
        }
    }
    val lunchStartTimes = mutableMapOf<Int, IntVar>()
    for (worker in workers) {
        lunchStartTimes[worker.id] = model.newIntVar(
            worker.startsWorking.toLong() + LUNCH_PAD_LEFT,
            worker.endsWorking.toLong() + LUNCH_PAD_RIGHT,
            "lunch_start_w${worker.id}"
        )
    }
    for (worker in workers) {
        val endOfLunch = LinearExpr.affine(lunchStartTimes[worker.id]!!, 1, 1)
        for (task in tasks) {
            val assigned = assignment[Pair(worker.id, task.id)]!!

            // model.newBoolVar("lunch_started")

            // model.addNoOverlap(listOf(
            //     model.newFixedSizeIntervalVar(lunchStartTimes[worker.id]!!, 1, "lunch_w${worker.id}_t${task.id}"),
            //     model.newFixedInterval(task.timeStart.toLong(), (task.timeEnd - task.timeStart).toLong(), "luncht_w${worker.id}_t${task.id}")
            // ))

            // Constraint(model.builder).builder

            model.addLessOrEqual(lunchStartTimes[worker.id]!!, task.timeEnd.toLong()).onlyEnforceIf(assigned)
            model.addGreaterOrEqual(endOfLunch, task.timeStart.toLong()).onlyEnforceIf(assigned)
        }
    }

    val unassignedTasks = model.newIntVar(0, tasks.size.toLong(), "unassigned_tasks")
    model.addMaxEquality(
        unassignedTasks, listOf(
            LinearExpr.sum(
                tasks.map { task ->
                    val anyWorkerAssigned = LinearExpr.sum(
                        workers.map { worker ->
                            assignment[Pair(worker.id, task.id)]!!
                        }.toTypedArray()
                    )
                    LinearExpr.affine(anyWorkerAssigned, -1, 1)
                }.toTypedArray()
            )
        )
    )


    model.minimize(unassignedTasks)
    println(model.validate())

    val solver = CpSolver()
    solver.parameters.setLogSearchProgress(true)
    val status = solver.solve(model)

    if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
        println("Solution:")
        for (worker in workers) {
            for (task in tasks) {
                if (solver.booleanValue(assignment[Pair(worker.id, task.id)]!!)) {
                    println("Worker ${worker.id} assigned to Task ${task.id} from ${task.timeStart} to ${task.timeEnd}")
                }
            }
            println("Worker ${worker.id} lunch break start: ${solver.value(lunchStartTimes[worker.id]!!)}")
        }
        println("Unassigned: " + solver.value(unassignedTasks))
    } else {
        println("No solution found, result: $status")
    }
}
