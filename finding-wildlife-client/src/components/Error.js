
function Error({ errors }) {
    return(
        <div className="alert alert-danger" role="alert">
        <ul>
            {errors.map(error => <li key={error}>{error}</li>)}
        </ul>
    </div>
    );
}

export default Error;