"use client"

import { useGlobalContext } from "../store/store"
import styles from './user.module.scss'
import Image from "next/image"
import { useRouter } from "next/navigation"
import { useEffect } from "react"
import axios from "axios"

export default function User() {
    const {user, setUser} = useGlobalContext()
    useEffect(() => {
        axios.get(`http://localhost:8080/api/user/email/${user?.email}`)
        .then(res => {
          setUser(
            {
              firstName: res.data.firstName,
              lastName: res.data.lastName,
              imageUrl: res.data.imageUrl,
              role: res.data.role,
              bonusPoints: res.data.bonusPoints,
              orders: res.data.orders,
              email: res.data.email,
              id: res.data.id
            }
          )
        })
        .catch(err => console.log(err)) 
    }, [])
    const { push } = useRouter()
    if (!user) {
        push('/login')
    }
    return (
        <section className={styles.main}>
                <Image className={styles.image} src={user?.imageUrl ? imageUrl : "/user.png"} width={350} height={350} alt="avatar image"/>   
                <div>
                    <h1>First name: {user?.firstName}</h1>
                    <h1>Last name: {user?.lastName}</h1>
                    <h1>Bonus points: {user?.bonusPoints}</h1>
                    <h1>Email: {user?.email}</h1>
                </div>
        </section>
    )
}