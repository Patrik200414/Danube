import PropTypes from 'prop-types';
import ProductsTable from "../component/product/ProductsTable";
import buttonObjectGenerator from "../utility/buttonObjectGenerator";
import useGetCartItems from "../utility/customHook/useGetCartItems";
import fetchDeleteAuthorization from "../utility/fetchDeleteAuthorization";
import getNavbarInformation from "../utility/getNavbarInformation";

function MyCart({onNavbarInformationChange}){
    const [cartItems, setCartItems, error] = useGetCartItems();

    async function handleDeleteion(orderId){
        const currUser = JSON.parse(sessionStorage.getItem('USER_JWT'));

        const removeItemResponse = await fetchDeleteAuthorization(`/api/cart/${orderId}`, currUser.jwt);

        if(removeItemResponse.ok){
            setCartItems(prev => prev.filter(order => order.id !== orderId));
            onNavbarInformationChange(getNavbarInformation(currUser));
        }
    }

    return (
        <div className="my-cart-dashboard">
            {error && <h1 className="error-message">{error}</h1>}
            {cartItems?.length ? <ProductsTable products={cartItems} buttons={[
                buttonObjectGenerator('removeItem', 'Remove item', true, '', (id) => handleDeleteion(id))
            ]} keyName={'orderId'}/> 
            : 
            cartItems !== undefined ? <h1 className="empty-cart-title">Your Danube Cart is empty!</h1> : ''}
            
        </div>
    )
}

MyCart.propTypes = {
    onNavbarInformationChange: PropTypes.func
}

export default MyCart;