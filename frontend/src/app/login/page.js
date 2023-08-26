
import styles from './page.module.scss'
import Login from './login'


export default function Home() {

    return (
        <main className={styles.container}>
            <Login/>
        </main>
    )
}