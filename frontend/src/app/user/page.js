"use client"

import { useGlobalContext } from '../store/store'
import styles from './page.module.scss'
import User from './user'
export default function Home() {

  const { isDark } = useGlobalContext()

  return (
    <main className={`${styles.main} ${isDark ? styles.dark : ""}`}>
      <User/>
   </main>

  )
}
