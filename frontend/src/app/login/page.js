"use client"

import styles from './page.module.scss'
import Login from './login'
import { useGlobalContext } from '../store/store'

export default function Home() {

    const {isDark} = useGlobalContext()

    return (
        <main className={`${styles.container} ${isDark ? styles.dark : ""}`}>
            <Login/>
        </main>
    )
}