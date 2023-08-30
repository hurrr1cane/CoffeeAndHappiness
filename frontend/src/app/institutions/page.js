import styles from './page.module.scss'
import InstitutionCards from './InstitutionCards'
export default function Home() {
  return (
   <main className={styles.main}>
      <h1>Institutions page</h1>
      <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nostrum corrupti aspernatur dolorem eligendi cum deleniti commodi rem nihil vitae saepe necessitatibus quos esse, blanditiis laboriosam nesciunt doloribus quasi officiis aliquam.</p>
      <InstitutionCards/>
    </main>

  )
}
