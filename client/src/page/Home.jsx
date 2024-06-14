import ProductContainer from "../component/product/ProductContainer";
import useFetch from "../utility/customHook/useFetch";

function Home(){
    const itemPerPage = 9;
    const pageNumber = 0;
    const [products, , error] = useFetch(`/api/product?pageNumber=${pageNumber}&itemPerPage=${itemPerPage}`);
    
    console.log(products);

    return(
        <div className="home">
            {error && <p className="error-message">{error}</p>}
            <ProductContainer products={products}/>
        </div>
    )
}

export default Home;