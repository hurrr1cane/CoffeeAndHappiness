import styles from './page.module.scss'
import VideoBackground from './components/VideoBackground/VideoBackground'
import News from './components/News/News'
export default function Home() {
  return (
    <div className={styles.container}>
    <VideoBackground>
    <main className={styles.main}>
      <h1>Main page</h1>
      <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Obcaecati commodi corporis soluta nisi aliquam pariatur hic, illum voluptate omnis mollitia, quas, natus at! Similique dolores accusantium laborum minima repellat! Officiis!</p>
    </main>
    </VideoBackground>
    
    <News/>
   </div>
  )
}
