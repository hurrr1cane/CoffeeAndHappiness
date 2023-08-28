import styles from "./MenuBar.module.scss";

export default function MenuBar({tab, setTab}) {
  
return (
    <nav className={styles.container}>
      <div onClick={() => {setTab("MAIN")}} className={`${styles.menuItem} ${tab === "MAIN" ? styles.selected : ''}`}>
        <p>Main</p>
      </div>
      <div onClick={() => {setTab("DRINK")}} className={`${styles.menuItem} ${tab === "DRINK" ? styles.selected : ''}`}>
        <p>Drinks</p>
      </div>
      <div onClick={() => {setTab("COFFEE")}} className={`${styles.menuItem} ${tab === "COFFEE" ? styles.selected : ''}`}>
        <p>Coffee</p>
      </div>
      <div onClick={() => {setTab("SALAD")}} className={`${styles.menuItem} ${tab === "SALAD" ? styles.selected : ''}`}>
        <p>Salads</p>
      </div>
      <div onClick={() => {setTab("DESSERT")}} className={`${styles.menuItem} ${tab === "DESERT" ? styles.selected : ''}`}>
        <p>Desserts</p>
      </div>
      <div onClick={() => {setTab("ICE_CREAM")}} className={`${styles.menuItem} ${tab === "ICE_CREAM" ? styles.selected : ''}`}>
        <p>Ice cream</p>
      </div>
    </nav>
  );
}
