import styles from './InstitutionCard.module.scss'
import Image from 'next/image'
import Link from 'next/link'
export default function InstitutionCard({ image }) {
    return (
    <div className={styles.card}>
        <Image
        src={image}
        className={styles.image}
        alt={'image of a restaurant'}
        />
        <p className={styles.description}>
            Location: вул. Шафарика 2A
            <br/>
            <br/>
            Working hours: 09:00 - 20:00
            <br/>
            <br/>
            <Link href="https://www.google.com/maps">View on a map</Link>
            <br/>
            <br/>
            <Link href={`/review/${''}`}>View reviews</Link>
        </p>
    </div>
    )
}