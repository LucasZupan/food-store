import { getSession, removeSession } from "./localStorage";
import { navigate } from "./navigate";

export type Rol = "ADMIN" | "USUARIO";

export const checkAuthUser = (
  redireccionLogin: string,
  redireccionSinPermiso: string,
  rol: Rol
) => {
  const session = getSession();

  if (!session) {
    navigate(redireccionLogin);
    return;
  }

  if (session.rol !== rol) {
    navigate(redireccionSinPermiso);
    return;
  }
};

export const logout = () => {
  removeSession();

  localStorage.removeItem("userData");
  localStorage.removeItem("users");

  navigate("/src/pages/auth/login/login.html");
};