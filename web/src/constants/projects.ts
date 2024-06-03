import VisualDriverImage from "$lib/images/project/visual_driver.jpg";
import MeteoServiceImage from "$lib/images/project/meteo_service.jpg";
import DataScienceImage from "$lib/images/project/data_science.jpg";
import WebCrawlerImage from "$lib/images/project/web_crawler.jpg";
import FtpDesktopUtilImage from "$lib/images/project/ftp_desktop_util.jpg";
import StableDiffusionImage from "$lib/images/project/stable_diffusion.jpg";
import TakiwadaiImage from "$lib/images/project/takiwadai.jpg";
import Log4jBombImage from "$lib/images/project/log4j_bomb.jpg";
import ParametricGraphicsImage from "$lib/images/project/parametric_graphics.jpg";
import MuseumImage from "$lib/images/project/museum.jpg";
import PeasCLIImage from "$lib/images/project/peas_cli.jpg";
import LeetcodeTrainingImage from "$lib/images/project/leetcode_training.jpg";
import WebPostParserImage from "$lib/images/project/web_post_parser.jpg";
import S3VultrDownloaderImage from "$lib/images/project/s3_vultr_downloader.jpg";
import WebK8SImage from "$lib/images/project/web_k8s.jpg";

const doubleRow = 'grid-row: span 2 / span 2;'
const doubleCol = 'grid-column: span 2 / span 2;'

export default [
    {
        background: VisualDriverImage,
        title: "VISUAL<br/>DRIVER",
        description: "Движок для Minecraft, общий набор визуальных решений",
        color: "--blue",
        blockSize: 314,
        url: "https://github.com/funcid/visual-driver"
    },
    {
        background: MeteoServiceImage,
        title: "METEO<br/>СЕРВИС",
        description: "Своя Я.Погода, от Arduino UNO и Spring Boot до Android",
        color: "--yellow",
        blockSize: 314,
        url: "https://github.com/funcid/meteo-service"
    },
    {
        background: DataScienceImage,
        title: "DATA<br/>SCIENCE",
        description: "Изучаю теор. вер., мат. стат. и ML",
        color: "--skyblue",
        blockSize: 273,
        url: "https://github.com/funcid/data-science"
    },
    {
        background: WebCrawlerImage,
        title: "WEB<br/>CRAWLER",
        description: "Сканнер интернета, сохраняет найденные сайты + поиск",
        color: "--bluefrost",
        blockSize: 314,
        url: "https://github.com/funcid/web-crawler"
    },
    {
        background: FtpDesktopUtilImage,
        title: "FTP<br/>DESKTOP<br/>UTIL",
        description: "Клиент для обмена файлами по FTP",
        color: "--yellow",
        blockSize: 311,
        height: 649,
        url: "https://github.com/funcid/ftp-desktop-util"
    },
    {
        background: StableDiffusionImage,
        title: "STABLE<br/>DIFFUSION<br/>INPAINT<br/>UTIL",
        description: "AI редактор фото на базе Stable Diffusion",
        color: "--green",
        blockSize: 444,
        height: 649,
        url: "https://github.com/funcid/stable-diffusion-inpaint-util"
    },
    {
        background: TakiwadaiImage,
        title: "TAKIWADAI",
        description: "Платформа для соревнований в решении олимп. задач",
        color: "--blue",
        blockSize: 208,
        height: 619,
        width: 1720,
        additionalStyle: doubleCol,
        url: "https://github.com/funcid/takiwadai"
    },
    {
        background: Log4jBombImage,
        title: "LOG4J<br/>BOMB<br/>EXPLOIT",
        description: "Proof of Concept: CVE-2021-44228 + fork bomb",
        color: "--yellow",
        blockSize: 403,
        height: 608,
        url: "https://github.com/funcid/log4j-exploit-fork-bomb"
    },
    {
        background: ParametricGraphicsImage,
        title: "PARAMETRIC<br/>GRAPHICS<br/>VISUALIZER",
        description: "Параметрическая графика, на базе JavaFX",
        color: "--skyblue",
        blockSize: 338,
        height: 1142,
        additionalStyle: doubleRow,
        url: "https://github.com/funcid/parametric-graphics-visualizer"
    },
    {
        background: MuseumImage,
        title: "MUSEUM",
        description: "Игра, экономический симулятор палеонтолога",
        color: "--purpur",
        blockSize: 249,
        height: 454,
        url: "https://github.com/cristalix-developers/museum"
    },

    {
        background: PeasCLIImage,
        title: "PEAS<br/>CLI",
        description: "Децентрализованный файлообмен (курсовая работа 2 курс)",
        color: "--pink",
        blockSize: 273,
        height: 737,
        width: 1720,
        additionalStyle: doubleCol,
        url: "https://github.com/funcid/peas-cli"
    },
    {
        background: LeetcodeTrainingImage,
        title: "LEETCODE<br/>TRAINING",
        description: "Решаю LeetCode проблемы",
        color: "--bluefrost",
        blockSize: 273,
        height: 543,
        url: "https://github.com/funcid/leetcode-training"
    },
    {
        background: WebPostParserImage,
        title: "WEB<br/>POST<br/>PARSER",
        description: "Посты из Pikabu/JoyReactor в XML/JSON",
        color: "--green",
        blockSize: 338,
        height: 543,
        url: "https://github.com/funcid/web-post-parser"
    },
    {
        background: S3VultrDownloaderImage,
        title: "S3<br/>VULTR<br/>DOWNLOADER",
        description: "Скачать корзину из Vultr",
        color: "--yellow",
        blockSize: 338,
        height: 543,
        url: "https://github.com/funcid/s3-vultr-downloader"
    },
    {
        background: WebK8SImage,
        title: "WEB-K8S",
        description: "Утилита для администрирования Kubernetes",
        color: "--blue",
        blockSize: 208,
        height: 543,
        url: "https://github.com/funcid/web-k8s"
    }
]
