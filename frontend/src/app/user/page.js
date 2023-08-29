import styles from './page.module.scss'
import User from './user'
export default function Home() {
  return (
   <main className={styles.main}>
      <User/>
   </main>

  )
}
