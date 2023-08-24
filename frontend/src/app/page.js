import styles from './page.module.scss'
import VideoBackground from './components/VideoBackgrounds'

export default function Home() {
  return (
    <div className={styles.container}>
    <VideoBackground>
    <main className={styles.main}>
      <h1>Main page</h1>
      <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Obcaecati commodi corporis soluta nisi aliquam pariatur hic, illum voluptate omnis mollitia, quas, natus at! Similique dolores accusantium laborum minima repellat! Officiis!</p>
    </main>
    </VideoBackground>
    <p className={styles.content}>
      Lorem ipsum dolor, sit amet consectetur adipisicing elit. Veniam nisi ad culpa facilis ratione architecto eum totam illo? Doloribus, neque provident eum atque eveniet cupiditate aut perferendis recusandae quae quis.
      Impedit iusto dolores sunt voluptatum, quam sapiente quae temporibus debitis dolorum voluptas, aliquid, tempora ducimus assumenda animi corrupti veritatis porro optio ipsa ea et. Libero assumenda tempore itaque quae cumque.
      Fuga, quasi, dicta veritatis atque in eum fugit blanditiis sapiente eaque aliquam id explicabo ipsa perferendis incidunt similique ipsam quam sit? Ex dolorem maiores totam consequuntur voluptatem omnis aliquam rem!
      Quae esse, porro eveniet laborum dolor quidem minus recusandae nulla veritatis fugiat inventore culpa! Sequi fuga suscipit non, incidunt, aut exercitationem saepe, expedita nihil nam debitis autem? Repellendus, voluptatum cumque!
      Harum possimus error atque quas laudantium provident sunt nam laboriosam recusandae veritatis sed fugiat natus, corrupti, non vel. Nemo accusamus laudantium vel provident laboriosam praesentium iure porro! Possimus, fugit voluptas?
    </p>
   </div>
  )
}
