"use client"

import { useGlobalContext } from "../store/store"
import styles from './user.module.scss'
import Image from "next/image"

export default function User() {
    const {user, setUser} = useGlobalContext()
    return (
        <section className={styles.main}>
                <Image src="/user.png" width={250} height={250} alt="avatar image"/>   
                <h1>{user?.firstName}</h1>
        </section>
    )
}