import ProductsTable from "../component/product/ProductsTable";
import buttonObjectGenerator from "../utility/buttonObjectGenerator";
import useGetCartItems from "../utility/customHook/useGetCartItems";

function MyCart(){

    const [cartItems, setCartItems, error] = useGetCartItems();

    return (
        <div className="my-cart-dashboard">
            {cartItems && <ProductsTable products={cartItems} buttons={[
                buttonObjectGenerator('removeItem', 'Remove item', true, '', () => console.log('Remove item'))
            ]}/>}
            
        </div>
    )
}

export default MyCart;