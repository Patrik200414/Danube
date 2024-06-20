import ProductsTable from "../component/product/ProductsTable";

import buttonObjectGenerator from '../utility/buttonObjectGenerator';
import useVerifyUserRole from "../utility/customHook/useVerifyUserRole";
import useFetchGetAuthorization from "../utility/customHook/useFetchGetAuthorization";
import useVerifyUserAccess from '../utility/customHook/useVerifyUserAccess';

function ProductUpdate(){
    useVerifyUserAccess('/verification/product/update');
    const [user] = useVerifyUserRole("ROLE_SELLER");
    const [myProducts, , error] = useFetchGetAuthorization(`/api/product/myProducts/${user ? user.id : ''}`, user);

    return(
        <div className="product-dashboard-container">
            {error && <h1 key={error} className="item-not-found-error">{error}</h1>}
            <>
                <h2>Product update</h2>
                {myProducts ? 
                    <ProductsTable products={myProducts.map(myProduct => ({image: myProduct.image, 'Product name': myProduct.productName, 'Owner': myProduct.owner, id: myProduct.id}))} buttons={[
                        buttonObjectGenerator('Update', 'Update', true, '/update'),
                        buttonObjectGenerator('Delete', 'Delete', true, '', (id) => console.log(`Delete ${id}`))
                    ]}/>
                    : 
                    <p className="loading-text">Loading...</p>}
            </>
        </div>
    )
}

export default ProductUpdate;