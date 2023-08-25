"use client"

import { usePathname } from "next/navigation"
import styles from './dish.module.scss'


export default function Dish() {

    const pathname = usePathname().split('dish/')[1]

    return (
        <main>
            <section className={styles.section}>
                <div className={styles.image}> Image placeholder, image id: {pathname}</div>
                <p>Image description: <br/> 
                Lorem ipsum dolor sit amet consectetur, adipisicing elit. 
                Ad aliquam officiis error dolor voluptate repellat, ullam non suscipit neque recusandae 
                cupiditate deleniti aliquid quibusdam incidunt deserunt optio facere sequi magni?</p>
            </section>
        </main>
    )
}