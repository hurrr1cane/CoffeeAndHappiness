"use client"

import { useGlobalContext } from "../store/store"
import Order from "./order";
import styles from './orders.module.scss'
import { Pagination } from "@mui/material";
import { useState } from "react";
export default function Orders() {
    const [page, setPage] = useState(1)

    // const { user, _} = useGlobalContext()

    // const orders = user.orders

    // Todo: Create a function that breaks down a list into objects with four list items  

    const [expanded, setExpanded] = useState(false);

    const handleChange = (panel) => (event, isExpanded) => {
      setExpanded(isExpanded ? panel : false);
    };


    function groupArrayByFour(inputArray) {
        const groupedArrays = [];
      
        for (let i = 0; i < inputArray.length; i += 5) {
          groupedArrays.push(inputArray.slice(i, i + 5));
        }
      
        return groupedArrays;
      }
      
      const orders = [
        { id: 1, name: "cappuccino", date: "18-09-2023" },
        { id: 2, name: "latte", date: "19-09-2023" },
        { id: 3, name: "espresso", date: "20-09-2023" },
        { id: 4, name: "mocha", date: "21-09-2023" },
        { id: 5, name: "americano", date: "22-09-2023" },
        { id: 6, name: "macchiato", date: "23-09-2023" },
        { id: 7, name: "chai latte", date: "24-09-2023" },
        { id: 8, name: "caramel macchiato", date: "25-09-2023" },
        { id: 9, name: "iced coffee", date: "26-09-2023" },
        { id: 10, name: "flat white", date: "27-09-2023" },
        { id: 11, name: "earl grey", date: "28-09-2023" }
      ];
      
      const groupedOrders = groupArrayByFour(orders);


      const displayedOrders = groupedOrders[page - 1] || [];


      return (
        <div className={styles['orders-container']}>
            <div className={styles['order-group']}>
            {displayedOrders.map((order) => (
            <Order
                key={order.id}
                expanded={expanded}
                handleChange={handleChange}
                name={order.name} 
                id={order.id}
                date={order.date}
               
            />
            ))}
        </div>
          <Pagination
            page={page}
            onChange={(event, value) => setPage(value)}
            count={Math.ceil(orders.length/5)}
            sx={{ alignSelf: "center", marginTop: "1rem"}}
            color="standard"
          />
        </div>
      );
    }