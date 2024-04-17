import defaultProductImage from '../static/defaultProduct.jpg';
import Proptypes from 'prop-types';

function ProductCard({product}){
    return(
        <div className="product-card">
            <img src={defaultProductImage}/>
            <h3>{product.productName}</h3>
            <h4>US ${product.price}</h4>
            <div className="sub-informations">
                <span>Shipping price: US ${product.shippingPrice}</span>
                <span>Sold: {product.sold}</span>
                <span>Quantity: {product.quantity}</span>
                <span>Delivery time in day: {product.deliveryTimeInDay}</span>
            </div>

        </div>
    )
}

ProductCard.propTypes = {
    product: Proptypes.object
}

export default ProductCard;