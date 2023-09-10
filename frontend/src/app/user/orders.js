"use client"

import { useGlobalContext } from "../store/store"
import Order from "./order";
import styles from './orders.module.scss'
import { Pagination } from "@mui/material";
import { useEffect, useState } from "react";
import useWindowSize from "../menu/dish/[id]/Reviews/useWindow";
import axios from "axios";
export default function Orders() {
    const [page, setPage] = useState(1)

    const { user, isDark} = useGlobalContext()

    const [orders, setOrders] = useState([])

    const {width, heigth} = useWindowSize()

    const [expanded, setExpanded] = useState(false);

    const handleChange = (panel) => (event, isExpanded) => {
      setExpanded(isExpanded ? panel : false);
    };

    useEffect(() => {
      axios.get('https://coffee-and-happiness-backend.azurewebsites.net/api/user/me', {
        headers: {
          Authorization: "Bearer " + user.token
        }
      })
      .then(res => setOrders(res.data.orders))
      .catch(err => console.log(err))
    }, [user?.token])




    function groupArrayByFour(inputArray) {
        const groupedArrays = [];
      
        for (let i = 0; i < inputArray.length; i += 5) {
          groupedArrays.push(inputArray.slice(i, i + 5));
        }
      
        return groupedArrays;
      }
      

      const groupedOrders = groupArrayByFour(orders);

      const displayedOrders = groupedOrders[page - 1] || [];


      return (
        <div className={styles['orders-container']}>
            <div className={styles['order-group']}>
            {orders.length >= 1 ? displayedOrders.map((order) => (
            <Order
                width = {width}
                key={order.id}
                expanded={expanded}
                handleChange={handleChange}
                order = {order}
                date={order.date}
                id={order.id}
               
            />
            )) : <h2 style={{fontWeight:400, marginBottom:"10rem", color: isDark ? "#CCCCCC" : "black"}}>No orders yet!</h2>}
        </div>
          {orders.length >= 1 && <Pagination
            page={page}
            onChange={(event, value) => setPage(value)}
            count={Math.ceil(orders.length/5)}
            sx={{ alignSelf: "center", marginTop: "1rem" }}
          />}
        </div>
      );
    }