import Link from "next/link"
import styles from "./Navbar.module.scss"

export default function Navbar() {
    return (
        <nav className={styles.navbar}>
            <Link className={styles.link} href="/"><h1>Home</h1></Link>
            <Link className={styles.link} href="/institutions"><h1>Institutions</h1></Link>
            <Link className={styles.link} href="/menu"><h1>Menu</h1></Link>
            <Link className={styles.link} href="/about-us"><h1>About us</h1> </Link>
            <Link className={styles.link} href="/user"><h1>User</h1> </Link>
        </nav>
    )
}