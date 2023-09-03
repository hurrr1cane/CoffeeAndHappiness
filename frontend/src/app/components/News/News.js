"use client"

import styles from './news.module.scss'
import axios from 'axios';
import { useEffect, useState } from 'react';
import Image from 'next/image';
function News() {

    const [news, setNews] = useState([])

    useEffect(() => {
        axios.get("http://localhost:8080/api/news")
        .then(res => setNews(res.data))
        .catch(err => console.log(err))
    })

  return (
    <div className={styles.container}>
        {news.map(newsItem =>(
            <div key={newsItem.id} className={styles.card}>
                <div className={styles.content}>
                    <Image className={styles.image} src={"https://coffee-and-happiness-images.s3.eu-central-1.amazonaws.com/coffee.jpeg"} width={400} height={200} alt="image of some news"/>
                    <h2>{newsItem.titleEN}</h2>
                    <p>{newsItem.descriptionEN}</p>
                </div>
            </div>
        ))}
    </div>
  );
}

export default News;