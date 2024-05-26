function ShippingInformationForm(){
    return(
        <form className="product-detail-form">
            <div>
                <label htmlFor="streetAddress">Street address: </label>
                <input type="text" name="streetAddress" id="streetAddress" placeholder="Street address..."/>
            </div>
            <div>
                <label htmlFor="city">City: </label>
                <input type="text" name="city" id="city" placeholder="City..."/>
            </div>
            <div>
                <label htmlFor="state">State/County: </label>
                <input type="text" name="state" id="state" placeholder="State/County..."/>
            </div>
            <div>
                <label htmlFor="zip">Zip/Postal code: </label>
                <input type="number" name="zip" id="zip" minLength="4" maxLength="12"/>
            </div>
            <div>
                <label htmlFor="country">Country: </label>
                <input type="text" name="country" id="country"/>
            </div>
        </form>
    )
}

export default ShippingInformationForm;