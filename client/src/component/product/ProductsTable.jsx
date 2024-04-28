import { Link } from "react-router-dom";

function ProductsTable({products, buttons}){
    return(
        <table>
            <thead>
                <tr>
                    {Object.keys(products[0]).map(fieldName => <th key={fieldName}>{fieldName}</th>)}
                    {buttons.map(button => <th key={button.actionName}>{button.actionName}</th>)}
                </tr>
            </thead>
        <tbody>
            {products.map(product => {
                return (
                    <tr key={product.id}>
                        {Object.keys(product).map(fieldName => <td key={`${fieldName}-${product.id}`}>{product[fieldName]}</td>)}
                        {buttons.map(button => (
                            <Link key={`${button.actionName}-${product.id}`} to={button.isDynamicLink ? `${button.linkTo}/${product.id}` : button.linkTo}>
                                <button onClick={() => button.onClick}>{button.buttonText}</button>
                            </Link>
                        ))}
                    </tr>
                )
            })}

            {/* {employees.map((employee) => (
            <tr key={employee._id}>
                <td>{employee.name}</td>
                <td>{employee.level}</td>
                <td>{employee.position}</td>
                <td>
                <Link to={`/update/${employee._id}`}>
                    <button type="button">Update</button>
                </Link>
                <button type="button" onClick={() => onDelete(employee._id)}>
                    Delete
                </button>
                </td>
            </tr>
            ))} */}
        </tbody>
        </table>
    )
}

export default ProductsTable;