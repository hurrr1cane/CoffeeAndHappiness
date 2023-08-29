"use client"

import { useGlobalContext } from "../store/store"

import styles from './orders.module.scss'
import { Pagination } from "@mui/material";
export default function Orders() {
    // const { user, _} = useGlobalContext()

    // const orders = user.orders

    const orders = [
        { name: "cappuccino", date: "18-09-2023" },
        { name: "latte", date: "19-09-2023" },
        { name: "espresso", date: "20-09-2023" },
        { name: "mocha", date: "21-09-2023" },
        // { name: "americano", date: "22-09-2023" },
        // { name: "macchiato", date: "23-09-2023" },
        // { name: "chai latte", date: "24-09-2023" },
        // { name: "caramel macchiato", date: "25-09-2023" },
        // { name: "iced coffee", date: "26-09-2023" },
        // { name: "flat white", date: "27-09-2023" },
      ];
      
    return (
        <div className={styles['orders-container']}>
            {orders.map(order => (
                <h2 className={styles.order}>{order.name}, {order.date}</h2>
            ))}
                  <Pagination count={10} sx={{alignSelf:"center", marginTop: "1rem"}} color="standard"/>
        </div>
    )
}