import styles from './page.module.scss'
import MenuBar from './MenuBar/MenuBar'
import dishPic from '../../../public/pizza.jpg'
import DishCard from './DishCard/DishCard'
// import DishCards from './DishCard/DishCards'
export default function Home() {
  return (
   <main className={styles.main}>
      <MenuBar/>
      <h1>Menu page</h1>
      <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Obcaecati commodi corporis soluta nisi aliquam pariatur hic, illum voluptate omnis mollitia, quas, natus at! Similique dolores accusantium laborum minima repellat! Officiis!</p>
      <DishCard image={dishPic}/>
      {/* <DishCards/> */}
   </main>

  )
}