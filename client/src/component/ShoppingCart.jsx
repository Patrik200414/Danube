import PropTypes from 'prop-types'
import { Link } from "react-router-dom";

function ShoppingCart({itemCount}){

    return(
        <Link to='/cart'>
            <div className="shopping-cart">
                <i className="fa fa-shopping-cart"></i>
                <p>{itemCount}</p>
            </div>
        </Link>
    )
}

ShoppingCart.propTypes = {
    itemCount: PropTypes.number
}

export default ShoppingCart;