import styles from './page.module.scss'
import Dish from './dish'
import Review from './Review'
export default function Home() {
    return (
    <div className={styles.main}>
        <h1>Dish page</h1>
        <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Obcaecati commodi corporis soluta nisi aliquam pariatur hic, illum voluptate omnis mollitia, quas, natus at! Similique dolores accusantium laborum minima repellat! Officiis!</p>
        <Dish/>
        <Review/>
     </div> 
    )
}