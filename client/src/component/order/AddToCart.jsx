import PropTypes from "prop-types";
import { useContext, useState } from "react";
import fetchPostAuthorizationFetch from '../../utility/fetchPostAuthorizationFetch';
import { NavbarContext } from "../../NavbarContext";

function AddToCart({maxQuantity, productId, onNavbarInformationChange}){
    const [selectedQuantity, setSelectedQuantity] = useState(1);
    const [user, ] = useState(JSON.parse(sessionStorage.getItem('USER_JWT')));
    const navbarInformation = useContext(NavbarContext)

    async function handleAddToCart(e){
        e.preventDefault();
        if(user){
            const order = {
                customerId: user.id,
                productId: productId,
                quantity: selectedQuantity
            }

            const cartData = await fetchPostAuthorizationFetch('/api/cart', user.jwt, JSON.stringify(order), true);
            if(cartData.ok){
                const newItem = await cartData.json();
                const navbarInfoCopy = {...navbarInformation};
                navbarInfoCopy.cartItems.push(newItem);

                onNavbarInformationChange(navbarInfoCopy);
            }
        }
    }

    return(
        <div className="order-quantity-add-to-cart-container">
            <form onSubmit={handleAddToCart}>
                <div>
                    <label htmlFor="itemQuantity">Quantity: </label>
                    <input type="number" id="itemQuantity" min="1" max={maxQuantity} value={selectedQuantity} onChange={e => {
                        const quantity = e.target.value;
                        if(quantity >= 0 && quantity <= maxQuantity){
                            setSelectedQuantity(quantity);
                        }
                    }}/>
                </div>
                <button className="submit">Add to cart</button>
            </form>
            
        </div>
    )
}

AddToCart.propTypes = {
    maxQuantity: PropTypes.number,
    productId: PropTypes.number,
    onNavbarInformationChange: PropTypes.func
}

export default AddToCart;