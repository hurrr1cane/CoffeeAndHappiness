import styles from './page.module.scss'
import Dish from './Dish'
import Reviews from './Reviews/Reviews'
export default function Home() {
    return (
    <div className={styles.main}>
        <Dish/>
        <h1 className={styles['reviews-header']}>Reviews:</h1>
        <Reviews/>
     </div> 
    )
}