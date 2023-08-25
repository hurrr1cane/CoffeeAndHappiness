import styles from './page.module.scss'
import MenuBar from './MenuBar/MenuBar'
import Dishes from './DishCard/Dishes'
export default function Home() {
  return (
   <main className={styles.main}>
      <MenuBar/>
      <h1>Menu page</h1>
      <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Obcaecati commodi corporis soluta nisi aliquam pariatur hic, illum voluptate omnis mollitia, quas, natus at! Similique dolores accusantium laborum minima repellat! Officiis!</p>
      
      <Dishes/>
   </main>

  )
}