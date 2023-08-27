import styles from './DishCard.module.scss'
import Image from 'next/image'
import { Rating } from '@mui/material'
import Link from 'next/link'
export default function DishCard(props) {
    return (
        <Link href={`/menu/dish/${props.id}`} className={styles.card}>
                <Image
                className={styles.image}
                src={props.imageUrl}
                height={200}
                width={200}
                alt='picture of a pizza'
                />
                <div>
                    <h1 className={styles.property}>{props.nameEN}</h1>
                    <p className={styles.property}>Description: {props.descriptionEN}</p>
                    <p className={styles.property}>Price: {props.price}</p>
                    <p className={styles.property}>Rating: <Rating sx={{top: "5px"}} name="read-only" value={Number(props.averageRating)} readOnly /></p>
                </div>
        </Link>
    )
}