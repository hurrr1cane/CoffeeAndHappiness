import styles from './DishCard.module.scss'
import Image from 'next/image'
import { Rating } from '@mui/material'
export default function DishCard(props) {
    return (
        <div className={styles.card}>
            <Image
            className={styles.image}
            src={props.image}
            alt='picture of a pizza'
            />
            <div>
                <h1 className={styles.property}>{props.name}</h1>
                <p className={styles.property}>Description: {props.description}</p>
                <p className={styles.property}>Price: {props.price}</p>
                <p className={styles.property}>Rating: <Rating sx={{top: "5px"}} name="read-only" value={Number(props.rating)} readOnly /></p>
            </div>
        </div>
    )
}