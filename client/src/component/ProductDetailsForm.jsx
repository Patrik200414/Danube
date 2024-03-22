import PropTypes from 'prop-types';

function ProductDetailsForm({onDetailsChange, productDetail}){
    return(
        <div className="ProductDetailsForm">
            <h2>Product details:</h2>
                <form className="product-detail-form">
                    <label htmlFor="productName">Product name: </label>
                    <input onChange={e => onDetailsChange(e.target.value, 'productName')} type="text" id="productName" name="productName" value={productDetail.productName} placeholder="Product name..."/>
                    <br />

                    <label htmlFor="brand">Brand name: </label>
                    <input onChange={e => onDetailsChange(e.target.value, 'brand')} type="text" id="brand" name="brand" value={productDetail.brand} placeholder="Drand name..."/>
                    <br />

                    <label htmlFor="price">Price: </label>
                    <input onChange={e => (!isNaN(e.target.value) && Number(e.target.value) >= 0) && onDetailsChange(e.target.value, 'price')} type="text" id="price" name="price" value={productDetail.price} placeholder="Price..."/>
                    <br />

                    <label htmlFor="shippingPrice">Shipping price: </label>
                    <input onChange={e => (!isNaN(e.target.value) && Number(e.target.value) >= 0) && onDetailsChange(e.target.value, 'shippingPrice')} type="text" id="shippingPrice" name="shippingPrice" value={productDetail.shippingPrice} placeholder="Shipping Price..."/>
                    <br />

                    <label htmlFor="quantity">Quantity: </label>
                    <input onChange={e => (!isNaN(e.target.value) && Number(e.target.value) >= 0 && Number.isInteger(Number(e.target.value)) && !e.target.value.includes('.')) && onDetailsChange(e.target.value, 'quantity')} type="text" id="quantity" name="quantity" value={productDetail.quantity} placeholder="Quantity..."/>
                    <br />

                    <label htmlFor="deliveryTimeInDay">Delivery time in days: </label>
                    <input onChange={e => (!isNaN(e.target.value) && Number(e.target.value) >= 0 && Number.isInteger(Number(e.target.value)) && !e.target.value.includes('.')) && onDetailsChange(e.target.value, 'deliveryTimeInDay')} type="text" id="deliveryTimeInDay" name="deliveryTimeInDay" value={productDetail.deliveryTimeInDay} placeholder="Delivery time in days..."/>
                    <br />

                    <label htmlFor="description">Description: </label>
                    <textarea onChange={e => onDetailsChange(e.target.value, 'description')} id="description" placeholder="Description...">{productDetail.description}</textarea>
                </form>
        </div>
    )
}


ProductDetailsForm.propTypes = {
    onDetailsChange: PropTypes.func,
    productDetail: PropTypes.object
}

export default ProductDetailsForm;