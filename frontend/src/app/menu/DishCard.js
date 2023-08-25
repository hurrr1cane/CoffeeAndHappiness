import styles from './DishCard.module.scss'
import Image from 'next/image'
export default function DishCard({ image }) {
    return (
        <div className={styles.card}>
            <Image
            className={styles.image}
            src={image}
            alt='picture of a pizza'
            />
            <p className={styles.description}>Description:
            <br/> Lorem ipsum dolor sit amet consectetur adipisicing elit. Dignissimos suscipit illo voluptatem quasi laborum itaque, error deleniti ipsam sapiente, vel dicta impedit aliquid incidunt voluptates, accusamus corporis optio earum sunt!
            Nulla facere dolorum labore sed, quia impedit pariatur tenetur corrupti nobis non harum quod architecto. Eius accusantium optio repellendus veritatis. Similique, fuga sunt nihil dicta at incidunt nisi! Unde, praesentium?</p>    
        </div>
    )
}