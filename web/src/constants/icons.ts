import Brackets from "$lib/images/icon/black_brackets.svg?raw";
import Dots from "$lib/images/icon/black_dots.svg?raw";
import Square from "$lib/images/icon/black_square.svg?raw";
import Lines from "$lib/images/icon/black_lines.svg?raw";
import Plus from "$lib/images/icon/black_plus.svg?raw";
import Flower from "$lib/images/icon/black_flower.svg?raw";
import Aim from "$lib/images/icon/black_aim.svg?raw";
import Angle from "$lib/images/icon/black_angle.svg?raw";
import Frog from "$lib/images/icon/black_frog.svg?raw";
import Bubble from "$lib/images/icon/black_bubble.svg?raw";
import Sadness from "$lib/images/icon/black_sadness.svg?raw";
import Link from "$lib/images/icon/link.svg?raw";

export const icons = [
    Brackets, Dots, Square, Lines, Plus, Flower, Aim, Angle, Frog, Bubble, Sadness, Link,
];

export function getRandomIcon() {
    return icons[Math.floor(Math.random() * icons.length)];
}