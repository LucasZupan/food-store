import { login } from "../../../services/auth.service";
import { saveSession } from "../../../utils/localStorage";
import { navigate } from "../../../utils/navigate";
import { showToast } from "../../../utils/toast";

const form = document.getElementById("form") as HTMLFormElement | null;
const inputEmail = document.getElementById("email") as HTMLInputElement | null;
const inputPassword = document.getElementById("password") as HTMLInputElement | null;

if (form && inputEmail && inputPassword) {
  form.addEventListener("submit", async (e: SubmitEvent) => {
    e.preventDefault();

    try {
      const response = await login({
        email: inputEmail.value.trim(),
        password: inputPassword.value,
      });

      const token = response.token;
      const payload = JSON.parse(atob(token.split(".")[1]));

      const rol = payload.rol as "ADMIN" | "USUARIO";

      saveSession({
        token,
        email: payload.sub,
        rol,
      });

      showToast("Login exitoso", "success");

      if (rol === "ADMIN") {
        navigate("/src/pages/admin/home/home.html");
      } else {
        navigate("/src/pages/store/home/home.html");
      }
    } catch (error) {
      showToast("Email o contraseña incorrectos", "error");
    }
  });
}