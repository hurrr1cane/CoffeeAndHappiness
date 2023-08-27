"use client"

import { usePathname } from "next/navigation"
import styles from './dish.module.scss'
import axios from "axios"
import { useEffect } from "react"

export default function Dish() {

    const pathname = usePathname().split('dish/')[1]

    useEffect(() => {
        axios.get(`http://localhost:8080/food/${pathname}`)
        .then(res => console.log(res))
        .catch(err => console.log(err))
    }, [])

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