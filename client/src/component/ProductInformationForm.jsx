import PropTypes from 'prop-types';

function ProductInformationForm({onDetailsChange, productDetail}){
    return(
        <div className="product-details-form-container">
            <h2>Product details:</h2>
                <form className="product-detail-form">
                    <label htmlFor="productName">Product name: </label>
                    <input onChange={e => onDetailsChange(e.target.value, 'Product name')} type="text" id="productName" name="productName" value={productDetail['Product name']} placeholder="Product name..."/>
                    <br />

                    <label htmlFor="brand">Brand name: </label>
                    <input onChange={e => onDetailsChange(e.target.value, 'Brand')} type="text" id="brand" name="brand" value={productDetail['Brand']} placeholder="Brand name..."/>
                    <br />

                    <label htmlFor="price">Price: </label>
                    <input onChange={e => (!isNaN(e.target.value) && Number(e.target.value) >= 0) && onDetailsChange(e.target.value, 'Price')} type="text" id="price" name="price" value={productDetail['Price']} placeholder="Price..."/>
                    <br />

                    <label htmlFor="shippingPrice">Shipping price: </label>
                    <input onChange={e => (!isNaN(e.target.value) && Number(e.target.value) >= 0) && onDetailsChange(e.target.value, 'Shipping price')} type="text" id="shippingPrice" name="shippingPrice" value={productDetail['Shipping price']} placeholder="Shipping Price..."/>
                    <br />

                    <label htmlFor="quantity">Quantity: </label>
                    <input onChange={e => (!isNaN(e.target.value) && Number(e.target.value) >= 0 && Number.isInteger(Number(e.target.value)) && !e.target.value.includes('.')) && onDetailsChange(e.target.value, 'Quantity')} type="text" id="quantity" name="quantity" value={productDetail.Quantity} placeholder="Quantity..."/>
                    <br />

                    <label htmlFor="deliveryTimeInDay">Delivery time in days: </label>
                    <input onChange={e => (!isNaN(e.target.value) && Number(e.target.value) >= 0 && Number.isInteger(Number(e.target.value)) && !e.target.value.includes('.')) && onDetailsChange(e.target.value, 'Delivery time in day')} type="text" id="deliveryTimeInDay" name="deliveryTimeInDay" value={productDetail['Delivery time in day']} placeholder="Delivery time in days..."/>
                    <br />

                    <label htmlFor="description">Description: </label>
                    <textarea onChange={e => onDetailsChange(e.target.value, 'Description')} id="description" placeholder="Description..." value={productDetail['Description']}></textarea>
                </form>
        </div>
    )
}


ProductInformationForm.propTypes = {
    onDetailsChange: PropTypes.func,
    productDetail: PropTypes.object
}

export default ProductInformationForm;