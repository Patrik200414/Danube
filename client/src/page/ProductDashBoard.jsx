import ProductsTable from "../component/product/ProductsTable";

import buttonObjectGenerator from '../utility/buttonObjectGenerator';
import useVerifyUser from "../utility/customHook/useVerifyUser";
import useFetchGetAuthorization from "../utility/customHook/useFetchGetAuthorization";

function ProductUpdate(){
    const [user] = useVerifyUser("ROLE_SELLER");
    const [myProducts, , error] = useFetchGetAuthorization(`/api/product/myProducts/${user ? user.id : ''}`, user);


    return(
        <div className="product-dashboard-container">
            {error && <h1 key={error} className="item-not-found-error">{error}</h1>}
            {myProducts &&
                <>
                    <h2>Product update</h2>
                    <ProductsTable products={myProducts} buttons={[
                        buttonObjectGenerator('Update', 'Update', true, '/update'),
                        buttonObjectGenerator('Delete', 'Delete', true, '', (id) => console.log(`Delete ${id}`))
                        ]}/>
                </>
            }
        </div>
    )
}

export default ProductUpdate;