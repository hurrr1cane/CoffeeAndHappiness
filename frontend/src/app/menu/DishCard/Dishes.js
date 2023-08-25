import dishPic from '../../../../public/pizza.jpg'
import styles from './Dishes.module.scss'
import DishCard from "./DishCard";
export default function Dishes() {
    const data = [{
        name:"Pizza",
        image: dishPic,
        description: "Appetizing pizza with italian dough",
        rating: "4",
        price:"$4.99"
    },
    {
        name: "Cappuccino",
        image: dishPic,
        description: "A rich and creamy espresso-based coffee topped with frothed milk.",
        rating: "4.8",
        price: "$3.99"
    },
    {
        name: "Croissant",
        image: dishPic,
        description: "Flaky and buttery French pastry that's perfect for breakfast.",
        rating: "4.5",
        price: "$2.49"
    },
    {
        name: "Avocado Toast",
        image: dishPic,
        description: "Sourdough toast topped with smashed avocado, red pepper flakes, and a poached egg.",
        rating: "4.6",
        price: "$5.99"
    },
    {
        name: "Chicken Panini",
        image: dishPic,
        description: "Grilled panini with seasoned chicken, melted cheese, and fresh vegetables.",
        rating: "4.4",
        price: "$7.49"
    },
    {
        name: "Mixed Berry Parfait",
        image: dishPic,
        description: "Layers of Greek yogurt, granola, and assorted fresh berries.",
        rating: "4.7",
        price: "$4.99"
    },
    {
        name: "Pesto Pasta",
        image: dishPic,
        description: "Penne pasta tossed in homemade basil pesto sauce and cherry tomatoes.",
        rating: "4.3",
        price: "$8.99"
    }]


    return (
        <div className={styles.container}>
            {data.map((item, index) => (
                <DishCard key={index} {...item} />
            ))}
        </div>
    )
}