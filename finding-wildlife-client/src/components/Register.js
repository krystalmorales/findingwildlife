import { useState } from 'react';
import { useHistory } from 'react-router-dom';
import Error from './Error';

const DEFAULT_REGISTER =  {
    username: '',
    password: '',
    confirmPassword: ''
}

function Register() {
    const [register, setRegister] = useState(DEFAULT_REGISTER);
    const [errors, setErrors] = useState([]);

    const history = useHistory();

    const handleSubmit = (event) => {
        event.preventDefault();

        if (register.password !== register.confirmPassword) {
            setErrors(['The passwords do not match.']);
            return;
        }

        const registration = {
            username: register.username,
            password: register.password
        };

        const init = {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registration)
        };

        fetch('http://localhost:8080/create_account', init)
        .then(response => {
            switch (response.status) {
                case 201:
                    history.push("/login");
                case 400:
                    return response.json();
                case 403:
                    return ['Unable to register with the provided information.']
                default:
                    return Promise.reject('Something terrible has happened here.');
            }
        })
        .then(body => {
            if (body && body.id) {
                history.push('/login')
            } else {
                setErrors(body);
            }
        })
        .catch(error => history.push('/error', {errors : error}));
    }

    const handleChange = (event) => {
        const registerCopy = {...register};

        registerCopy[event.target.name] = event.target.value;

        setRegister(registerCopy);
    }

    return (<>
    <div className='register-container'>
        <h2 className="pt-3 text-center">Register</h2>
        {errors.length > 0 ? <Error errors={errors} /> : null}
        <form onSubmit={handleSubmit}>
            <div className="pt-3 form-group">
                <label htmlFor='username'>Username</label>
                <input name='username' type="text" className='form-control' id='username' value={register.username} onChange={handleChange} />
            </div>
            <div className="pt-3 form-group">
                <label htmlFor='password'>Password</label>
                <input name='password' type="password" className='form-control' id='password' value={register.password} onChange={handleChange}/>
                <small id="passwordHelp" className="form-text text-muted">Must be atleast 8 characters long and include 1 letter, 1 number, and 1 special character.</small>
            </div>
            <div className="pt-3 form-group">
                <label htmlFor='confirmPassword'> Confirm Password</label>
                <input name='confirmPassword' type="password" className='form-control' id='confirmPassword' value={register.confirmPassword} onChange={handleChange}/>
            </div>
            <div className="pt-3 form-group">
                <button type='submit' className='btn btn-primary'>Register</button>
            </div>
        </form>
        </div>
    </>);
}

export default Register;