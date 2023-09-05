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

    const { user, _} = useGlobalContext()

    const {width, heigth} = useWindowSize()

    const [expanded, setExpanded] = useState(false);

    const handleChange = (panel) => (event, isExpanded) => {
      setExpanded(isExpanded ? panel : false);
    };

    useEffect(() => {
      axios.get('http://localhost:8080/api/user/me', {
        headers: {
          Authorization: "Bearer " + user.token
        }
      })
      .then(res => console.log(res.data.orders))
      .catch(err => console.log(err))
    }, [])

    function convertTimestampToFormattedDate(timestampString) {
      const date = new Date(timestampString);
    
      const day = date.getDate().toString().padStart(2, '0');
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const year = date.getFullYear();
    
      const formattedDate = `${day}-${month}-${year}`;
      return formattedDate;
    }


    function groupArrayByFour(inputArray) {
        const groupedArrays = [];
      
        for (let i = 0; i < inputArray.length; i += 5) {
          groupedArrays.push(inputArray.slice(i, i + 5));
        }
      
        return groupedArrays;
      }
      
      // const orders = [
      //   { id: 1, name: "cappuccino", date: "18-09-2023" },
      //   { id: 2, name: "latte", date: "19-09-2023" },
      //   { id: 3, name: "espresso", date: "20-09-2023" },
      //   { id: 4, name: "mocha", date: "21-09-2023" },
      //   { id: 5, name: "americano", date: "22-09-2023" },
      //   { id: 6, name: "macchiato", date: "23-09-2023" },
      //   { id: 7, name: "chai latte", date: "24-09-2023" },
      //   { id: 8, name: "caramel macchiato", date: "25-09-2023" },
      //   { id: 9, name: "iced coffee", date: "26-09-2023" },
      //   { id: 10, name: "flat white", date: "27-09-2023" },
      //   { id: 11, name: "earl grey", date: "28-09-2023" }
      // ];
      
      const orders = []

      const groupedOrders = groupArrayByFour(orders);


      const displayedOrders = groupedOrders[page - 1] || [];


      return (
        <div className={styles['orders-container']}>
            <div className={styles['order-group']}>
            {orders.length > 1 ? displayedOrders.map((order) => (
            <Order
                width = {width}
                key={order.id}
                expanded={expanded}
                handleChange={handleChange}
                name={order.name} 
                id={order.id}
                date={order.date}
               
            />
            )) : <h2 style={{fontWeight:400, marginBottom:"10rem"}}>No orders yet!</h2>}
        </div>
          {orders.length > 1 && <Pagination
            page={page}
            onChange={(event, value) => setPage(value)}
            count={Math.ceil(orders.length/5)}
            sx={{ alignSelf: "center", marginTop: "1rem"}}
            color="standard"
          />}
        </div>
      );
    }