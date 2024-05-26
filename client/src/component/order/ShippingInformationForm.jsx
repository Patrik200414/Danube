import PropTypes from 'prop-types';

function ShippingInformationForm({shippingInformation, onShippingIformationChange}){
    return(
        <form className="product-detail-form">
            <div>
                <label htmlFor="streetAddress">Street address: </label>
                <input onChange={e => onShippingIformationChange('streetAddress', e.target.value)} type="text" name="streetAddress" id="streetAddress" placeholder="Street address..." value={shippingInformation.streetAddress}/>
            </div>
            <div>
                <label htmlFor="city">City: </label>
                <input onChange={e => onShippingIformationChange('city', e.target.value)} type="text" name="city" id="city" placeholder="City..." value={shippingInformation.city}/>
            </div>
            <div>
                <label htmlFor="state">State/County: </label>
                <input onChange={e => onShippingIformationChange('state', e.target.value)} type="text" name="state" id="state" placeholder="State/County..." value={shippingInformation.state}/>
            </div>
            <div>
                <label htmlFor="zip">Zip/Postal code: </label>
                <input onChange={e => onShippingIformationChange('zip', e.target.value)} type="number" name="zip" id="zip" minLength="4" maxLength="12" value={shippingInformation.zip}/>
            </div>
            <div>
                <label htmlFor="country">Country: </label>
                <input onChange={e => onShippingIformationChange('country', e.target.value)} type="text" name="country" id="country" value={shippingInformation.country}/>
            </div>
        </form>
    )
}

ShippingInformationForm.propTypes = {
    shippingInformation: PropTypes.object,
    onShippingIformationChange: PropTypes.func
}

export default ShippingInformationForm;