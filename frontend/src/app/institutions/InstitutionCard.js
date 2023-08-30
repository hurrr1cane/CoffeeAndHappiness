import styles from './InstitutionCard.module.scss'
import Image from 'next/image'
import Link from 'next/link'

export default function InstitutionCard({ cafe }) {
    return (
    <div className={styles.card}>
        <Image
        src={cafe?.imageUrl}
        className={styles.image}
        alt={'image of a restaurant'}
        height={200}
        width={200}
        />
        <div className={styles.description}>
            <p>{cafe?.locationEN}</p>
            <p>Phone number: {cafe.phoneNumber}</p>
            <p>
                <Link href="https://www.google.com/maps">View on a map</Link>
            </p>
            <p>
                <Link href={`institutions/institution/${cafe?.id}`}>View reviews</Link>
            </p>
        </div>  
        
    </div>
    )
}