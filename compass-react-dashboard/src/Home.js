import React, { Component, Suspense  } from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import { Link, Redirect, Route, HashRouter, Switch } from 'react-router-dom';
import { Button, Container } from 'reactstrap';
import { withCookies } from 'react-cookie';

import Loadable from 'react-loadable';


import {
  AppAside,
  AppBreadcrumb,
  AppFooter,
  AppHeader,
  AppSidebar,
  AppSidebarFooter,
  AppSidebarForm,
  AppSidebarHeader,
  AppSidebarMinimizer,
  AppSidebarNav,
} from '@coreui/react';
// sidebar nav config
import navigation from './_nav';
// routes config
import routes from './routes';

const DefaultAside = React.lazy(() => import('./containers/DefaultLayout/DefaultAside'));
const DefaultFooter = React.lazy(() => import('./containers/DefaultLayout/DefaultFooter'));
const DefaultHeader = React.lazy(() => import('./containers/DefaultLayout/DefaultHeader'));

const loading = () => <div className="animated fadeIn pt-3 text-center">Loading...</div>;

// Containers
const DefaultLayout = Loadable({
  loader: () => import('./containers/DefaultLayout'),
  loading
});


// Pages
const Login = Loadable({
  loader: () => import('./views/Pages/Login'),
  loading
});

const Register = Loadable({
  loader: () => import('./views/Pages/Register'),
  loading
});

const Page404 = Loadable({
  loader: () => import('./views/Pages/Page404'),
  loading
});

const Page500 = Loadable({
  loader: () => import('./views/Pages/Page500'),
  loading
});

class Home extends Component {
  state = {
    isLoading: true,
    isAuthenticated: false,
    user: undefined
  };

  constructor(props) {
    super(props);
    const {cookies} = props;
    this.state.csrfToken = cookies.get('XSRF-TOKEN');
    this.login = this.login.bind(this);
    this.logout = this.logout.bind(this);
  }

  async componentDidMount() {
    const response = await fetch('/api/user', {credentials: 'include'});
    const body = await response.text();
    if (body === '') {
      this.setState(({isAuthenticated: false}))
    } else {
      this.setState({isAuthenticated: true, user: JSON.parse(body)})
    }
  }

  login() {
    let port = (window.location.port ? ':' + window.location.port : '');
    if (port === ':3000') {
      port = ':8080';
    }
    window.location.href = '//' + window.location.hostname + port + '/private';
  }

  logout() {
    fetch('/api/logout', {method: 'POST', credentials: 'include',
      headers: {'X-XSRF-TOKEN': this.state.csrfToken}}).then(res => res.json())
      .then(response => {
        window.location.href = response.logoutUrl + "?id_token_hint=" +
          response.idToken + "&post_logout_redirect_uri=" + window.location.origin;
      });
  }

  manageGroups = () => {
    this.props.history.push('/groups');
  };

  render() {
    const message = this.state.user ?
      <h2>Welcome, {this.state.user.name}!
      </h2> :
      <p>Please log in to manage your JUG Tour.</p>;

    const button = this.state.isAuthenticated ?
      <div>
        {/* Firefox - cannot nest link insdie button <Button color="link"> <Link to="/groups">Manage JUG Tour</Link></Button> */}
        <Link to="/groups">Manage JUG Tour link</Link>
        <br/>
       <Button color="link" onClick={this.manageGroups}>Manage JUG Tour button</Button>
        <br/>
        <Button color="link" onClick={this.logout}>Logout</Button>
      </div> :
      <Button color="primary" onClick={this.login}>Login</Button>;

    return (
       <div>
         <AppNavbar/>
         <Container fluid>
           {message}
           {button}

         <div className="app-body">
                   <AppSidebar fixed display="lg">
                     <AppSidebarHeader />
                     <AppSidebarForm />
                     <Suspense>
                     <AppSidebarNav navConfig={navigation} {...this.props} />
                     </Suspense>
                     <AppSidebarFooter />
                     <AppSidebarMinimizer />
                   </AppSidebar>
                 </div>

                 </Container>
       </div>
    );
  }
}

export default withCookies(Home);
