import { useState } from "react";
import { Link } from "react-router-dom";

function ShoppingCart(){
    const [itemCount, ] = useState(JSON.parse(sessionStorage.getItem('ITEMS_IN_CART')) === null ? 0 : JSON.parse(sessionStorage.getItem('ITEMS_IN_CART')).length);

    return(
        <Link to='/cart'>
            <div className="shopping-cart">
                <i className="fa fa-shopping-cart"></i>
                <p>{itemCount}</p>
            </div>
        </Link>
    )
}


export default ShoppingCart;