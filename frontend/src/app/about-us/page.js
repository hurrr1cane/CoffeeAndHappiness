"use client"

import styles from './page.module.scss'
import { useGlobalContext } from '../store/store'
export default function Home() {

  const { isDark } = useGlobalContext()

  return (
   <main className={`${styles.main} ${isDark ? styles.dark : ""}`}>
      <h1>About page</h1>
      <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Obcaecati commodi corporis soluta nisi aliquam pariatur hic, illum voluptate omnis mollitia, quas, natus at! Similique dolores accusantium laborum minima repellat! Officiis!</p>
   </main>

  )
}
