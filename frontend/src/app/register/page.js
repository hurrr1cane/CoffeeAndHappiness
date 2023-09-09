"use client"

import styles from './page.module.scss'

import Register from './register'
import { useGlobalContext } from '../store/store'
export default function Home() {

  const {isDark} = useGlobalContext()

  return (
    <main className={`${styles.main} ${isDark ? styles.dark : ""}`}>
      <Register/>
   </main>

  )

}