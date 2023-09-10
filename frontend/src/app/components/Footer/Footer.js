"use client"

import Icons from "./Icons"
import styles from './Footer.module.scss'
import { useGlobalContext } from "@/app/store/store"
function getYear() {
    return new Date().getFullYear() 
}

export default function Footer() {
    const { isDark } = useGlobalContext()

    return (
            <footer className={`${styles.footer} ${ isDark ? styles.dark : ""}`}>
                <p>With Love</p>
                <p>Coffee and Happiness © {getYear()}</p>
                <div className={styles.icons}>
                <Icons/>
                </div>
            </footer>
    )
}