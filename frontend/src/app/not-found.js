import styles from './not-found.module.scss'

export default function Custom404() {
    return (
        <div className={styles['error-div']}>
            <h1 className={styles['error-h1']}>
                404
            </h1>
            <div className={styles.line}></div>
            <div className={styles.div}>
                <h2 className={styles['error-h2']}>This page could not be found.</h2>
            </div>
        </div>
    )
}