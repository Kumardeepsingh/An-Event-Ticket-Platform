import { useEffect } from "react";
import { useAuth } from "react-oidc-context";
import { useLocation } from "react-router";

const LoginPage: React.FC = () => {
  const { isLoading, isAuthenticated, signinRedirect } = useAuth();
  const location = useLocation();

  useEffect(() => {
    if (isLoading) {
      return;
    }
    if (!isAuthenticated) {
      const from = (location.state as { from?: { pathname: string } })?.from
        ?.pathname;
      if (from) {
        localStorage.setItem("redirectPath", from);
      }
      signinRedirect();
    }
  }, [isLoading, isAuthenticated, signinRedirect, location]);

  return <div>Redirecting to login...</div>;
};

export default LoginPage;