"use client"

import { usePathname } from "next/navigation"
import styles from './dish.module.scss'


export default function Dish() {

    const pathname = usePathname().split('dish/')[1]

    return (
        <h2>{pathname}</h2>
    )
}