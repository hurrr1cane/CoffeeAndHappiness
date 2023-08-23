import Icons from "./Icons"
import styles from './Footer.module.scss'
function getYear() {
    return new Date().getFullYear() 
}

export default function Footer() {
    return (
            <footer className={styles.footer}>
                <p>With Love</p>
                <p>Coffee and Happiness © {getYear()}</p>
                <div className={styles.icons}>
                <Icons/>
                </div>
            </footer>
    )
}