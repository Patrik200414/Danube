import PropTypes from 'prop-types';
import ProductsTable from "../component/product/ProductsTable";
import useGetCartItems from "../utility/customHook/useGetCartItems";
import { useContext } from 'react';
import { NavbarContext } from '../NavbarContext';
import { Link } from 'react-router-dom';
import { fetchDeleteAuthorization } from '../utility/fetchUtilities';
import { buttonObjectGenerator } from '../utility/componentUtilities';


function MyCart({onNavbarInformationChange}){
    const [cartItems, setCartItems, error] = useGetCartItems();
    const navbarInfo = useContext(NavbarContext);


    function handleDeleteion(orderId){
        const currUser = JSON.parse(sessionStorage.getItem('USER_JWT'));
        if(currUser){
            handleRegisteredDeletion(orderId, currUser);
        } else{
            handleUnregisteredDeletion(orderId);
        } 
    }

    async function handleRegisteredDeletion(orderId, currUser){
        const removeItemResponse = await fetchDeleteAuthorization(`/api/cart/${orderId}`, currUser.jwt);

        if(removeItemResponse.ok){
            let deletedItemQuantity = 0;
            const keptItems = [];
            cartItems.forEach(item => {
                if(item.id !== orderId){
                    keptItems.push(item);
                } else{
                    deletedItemQuantity = item.orderedQuantity;
                }
            });
            setCartItems(keptItems);
            onNavbarInformationChange({
                userFirstName: currUser.firstName,
                cartItemNumber: navbarInfo.cartItemNumber - deletedItemQuantity
            });
        }
    }

    function handleUnregisteredDeletion(orderId){
        let deletedItem;
        const updatedCartItems = cartItems.filter(item => {
            if(item.id !== orderId){
                return true
            } else{
                deletedItem = item;
                return false;
            }
        });
        setCartItems(updatedCartItems);
        localStorage.setItem('CART_ITEMS', JSON.stringify(updatedCartItems));
        onNavbarInformationChange({...navbarInfo, cartItemNumber: navbarInfo.cartItemNumber - deletedItem.orderedQuantity});
    }

    return (
        <div className="my-cart-dashboard">
            {error && <h1 className="error-message">{error}</h1>}
            {cartItems?.length ? <>
                <ProductsTable products={cartItems} buttons={[
                    buttonObjectGenerator('removeItem', 'Remove item', true, '', (id) => handleDeleteion(id))
                ]} keyName={'orderId'}/> 

                <Link to='/user/checkout'>
                    <button className='submit-button'>Checkout!</button>
                </Link>
            </>
            : 
            cartItems !== undefined ? <h1 className="empty-cart-title">Your Danube Cart is empty!</h1> : ''}
            
        </div>
    )
}

MyCart.propTypes = {
    onNavbarInformationChange: PropTypes.func
}

export default MyCart;